import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity('orders')
export class Order {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  orderNo: string;

  @Column()
  publisherId: number;

  @Column({ nullable: true })
  acceptorId: number;

  @Column()
  type: string; // 'deliver' | 'pickup'

  @Column({ default: 'pending' })
  status: string; // 'pending' | 'accepted' | 'picked' | 'delivering' | 'completed' | 'cancelled'

  @Column({ type: 'text' })
  pickupAddress: string;

  @Column('decimal', { precision: 10, scale: 8, nullable: true })
  pickupLat: number;

  @Column('decimal', { precision: 11, scale: 8, nullable: true })
  pickupLng: number;

  @Column({ type: 'text' })
  deliveryAddress: string;

  @Column('decimal', { precision: 10, scale: 8, nullable: true })
  deliveryLat: number;

  @Column('decimal', { precision: 11, scale: 8, nullable: true })
  deliveryLng: number;

  @Column({ type: 'text', nullable: true })
  description: string;

  @Column('simple-array', { nullable: true })
  images: string[];

  @Column('decimal', { precision: 10, scale: 2 })
  reward: number;

  @Column({ nullable: true })
  expectedTime: string;

  @Column({ nullable: true })
  acceptedAt: Date;

  @Column({ nullable: true })
  pickedAt: Date;

  @Column({ nullable: true })
  completedAt: Date;

  @Column({ nullable: true })
  publisherRating: number;

  @Column({ nullable: true })
  acceptorRating: number;

  @Column({ type: 'text', nullable: true })
  publisherComment: string;

  @Column({ type: 'text', nullable: true })
  acceptorComment: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
