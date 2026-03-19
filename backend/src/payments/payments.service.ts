import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Payment } from './payment.entity';

@Injectable()
export class PaymentsService {
  constructor(
    @InjectRepository(Payment)
    private readonly paymentRepository: Repository<Payment>,
  ) {}

  async findByUser(userId: number): Promise<Payment[]> {
    return this.paymentRepository.find({
      where: { userId },
      order: { createdAt: 'DESC' },
      take: 50,
    });
  }

  async create(createPaymentDto: any): Promise<Payment> {
    const result = await this.paymentRepository.insert(createPaymentDto);
    return this.paymentRepository.findOne({ where: { id: result.identifiers[0].id } }) as Promise<Payment>;
  }
}
