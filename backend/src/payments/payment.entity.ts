import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne } from 'typeorm';
import { Order } from '../orders/order.entity';

@Entity('payments')
export class Payment {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  orderId: number;

  @ManyToOne(() => Order, { nullable: true })
  order: Order;

  @Column()
  userId: number;

  @Column()
  type: string; // 'pay' | 'refund' | 'withdraw' | 'reward'

  @Column('decimal', { precision: 10, scale: 2 })
  amount: number;

  @Column()
  status: string; // 'pending' | 'success' | 'failed'

  @Column({ nullable: true })
  transactionId: string;

  @Column({ type: 'text', nullable: true })
  description: string;

  @CreateDateColumn()
  createdAt: Date;
}
