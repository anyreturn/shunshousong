import { Controller, Get, Post, Body, Param, ParseIntPipe } from '@nestjs/common';
import { PaymentsService } from './payments.service';
import { Payment } from './payment.entity';

@Controller('api/payments')
export class PaymentsController {
  constructor(private readonly paymentsService: PaymentsService) {}

  @Get(':userId')
  findByUser(@Param('userId', ParseIntPipe) userId: number): Promise<Payment[]> {
    return this.paymentsService.findByUser(userId);
  }

  @Post()
  create(@Body() createPaymentDto: any): Promise<Payment> {
    return this.paymentsService.create(createPaymentDto);
  }
}
