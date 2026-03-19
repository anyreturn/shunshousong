import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, BeforeInsert, BeforeUpdate } from 'typeorm';
import * as bcrypt from 'bcryptjs';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true, nullable: true })
  openid: string;

  @Column({ unique: true, nullable: true })
  phone: string;

  @Column({ nullable: true })
  email: string;

  @Column({ nullable: true })
  password: string;

  @Column()
  nickname: string;

  @Column({ nullable: true })
  avatar: string;

  @Column({ nullable: true })
  realName: string;

  @Column({ nullable: true })
  idCard: string;

  @Column('decimal', { precision: 10, scale: 2, default: 0 })
  deposit: number;

  @Column('decimal', { precision: 10, scale: 2, default: 0 })
  balance: number;

  @Column({ default: 100 })
  creditScore: number;

  @Column({ default: false })
  isVerified: boolean;

  @Column({ default: 0 })
  completedOrders: number;

  @Column({ default: 0 })
  totalEarnings: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  // 密码加密钩子
  @BeforeInsert()
  @BeforeUpdate()
  async hashPassword() {
    if (this.password && !this.password.startsWith('$2')) {
      const salt = await bcrypt.genSalt(10);
      this.password = await bcrypt.hash(this.password, salt);
    }
  }

  // 验证密码方法
  async validatePassword(password: string): Promise<boolean> {
    if (!this.password) return false;
    return bcrypt.compare(password, this.password);
  }
}
