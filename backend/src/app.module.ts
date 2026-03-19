import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { OrdersModule } from './orders/orders.module';
import { UsersModule } from './users/users.module';
import { Order } from './orders/order.entity';
import { User } from './users/user.entity';
import { PaymentsModule } from './payments/payments.module';
import { Payment } from './payments/payment.entity';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'sqlite',
      database: 'shunshousong.db',
      entities: [Order, User, Payment],
      synchronize: true, // 生产环境应设为 false
      logging: process.env.NODE_ENV === 'development',
    }),
    OrdersModule,
    UsersModule,
    PaymentsModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
