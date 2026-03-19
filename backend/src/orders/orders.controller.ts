import { Controller, Get, Post, Body, Param, ParseIntPipe, Query } from '@nestjs/common';
import { OrdersService } from './orders.service';
import { Order } from './order.entity';

@Controller('api/orders')
export class OrdersController {
  constructor(private readonly ordersService: OrdersService) {}

  @Get()
  findAll(
    @Query('status') status?: string,
    @Query('type') type?: string,
    @Query('limit') limit?: number,
  ): Promise<Order[]> {
    return this.ordersService.findAll({ status, type, limit: limit || 20 });
  }

  @Get(':id')
  findOne(@Param('id', ParseIntPipe) id: number): Promise<Order> {
    return this.ordersService.findOne(id);
  }

  @Post()
  create(@Body() createOrderDto: any): Promise<Order> {
    return this.ordersService.create(createOrderDto);
  }

  @Post(':id/accept')
  accept(@Param('id', ParseIntPipe) id: number, @Body() body: any): Promise<Order> {
    return this.ordersService.accept(id, body.acceptorId);
  }

  @Post(':id/pick')
  pick(@Param('id', ParseIntPipe) id: number): Promise<Order> {
    return this.ordersService.pick(id);
  }

  @Post(':id/complete')
  complete(@Param('id', ParseIntPipe) id: number, @Body() body: any): Promise<Order> {
    return this.ordersService.complete(id, body.rating, body.comment);
  }

  @Post(':id/cancel')
  cancel(@Param('id', ParseIntPipe) id: number): Promise<Order> {
    return this.ordersService.cancel(id);
  }
}
