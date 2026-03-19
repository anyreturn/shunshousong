import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Order } from './order.entity';

@Injectable()
export class OrdersService {
  constructor(
    @InjectRepository(Order)
    private readonly orderRepository: Repository<Order>,
  ) {}

  async findAll(filters: { status?: string; type?: string; limit: number }): Promise<Order[]> {
    const queryBuilder = this.orderRepository.createQueryBuilder('order');
    
    if (filters.status) {
      queryBuilder.andWhere('order.status = :status', { status: filters.status });
    }
    if (filters.type) {
      queryBuilder.andWhere('order.type = :type', { type: filters.type });
    }
    
    return queryBuilder
      .orderBy('order.createdAt', 'DESC')
      .limit(filters.limit)
      .getMany();
  }

  async findOne(id: number): Promise<Order> {
    const order = await this.orderRepository.findOne({ where: { id } });
    if (!order) {
      throw new NotFoundException(`订单 ${id} 不存在`);
    }
    return order;
  }

  async create(createOrderDto: any): Promise<Order> {
    const orderData = {
      ...createOrderDto,
      orderNo: `SS${Date.now()}${Math.random().toString(36).substr(2, 6).toUpperCase()}`,
      status: 'pending',
    };
    const result = await this.orderRepository.insert(orderData);
    return this.findOne(result.identifiers[0].id);
  }

  async accept(id: number, acceptorId: number): Promise<Order> {
    await this.orderRepository.update(id, {
      acceptorId,
      status: 'accepted',
      acceptedAt: new Date(),
    });
    return this.findOne(id);
  }

  async pick(id: number): Promise<Order> {
    await this.orderRepository.update(id, {
      status: 'picked',
      pickedAt: new Date(),
    });
    return this.findOne(id);
  }

  async complete(id: number, rating?: number, comment?: string): Promise<Order> {
    const updateData: any = {
      status: 'completed',
      completedAt: new Date(),
    };
    if (rating) updateData.publisherRating = rating;
    if (comment) updateData.publisherComment = comment;
    
    await this.orderRepository.update(id, updateData);
    return this.findOne(id);
  }

  async cancel(id: number): Promise<Order> {
    await this.orderRepository.update(id, {
      status: 'cancelled',
    });
    return this.findOne(id);
  }
}
