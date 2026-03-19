import { Injectable, NotFoundException, ConflictException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './user.entity';
import { CreateUserDto, RegisterDto, LoginDto } from './dto/create-user.dto';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private readonly userRepository: Repository<User>,
  ) {}

  async findAll(): Promise<User[]> {
    return this.userRepository.find({
      order: { createdAt: 'DESC' },
      take: 50,
    });
  }

  async findOne(id: number): Promise<User> {
    const user = await this.userRepository.findOne({ where: { id } });
    if (!user) {
      throw new NotFoundException(`用户 ${id} 不存在`);
    }
    return user;
  }

  async create(createUserDto: CreateUserDto): Promise<User> {
    const existing = await this.userRepository.findOne({
      where: { openid: createUserDto.openid },
    });
    if (existing) {
      return existing;
    }
    const result = await this.userRepository.insert(createUserDto);
    return this.findOne(result.identifiers[0].id);
  }

  /**
   * 用户注册
   */
  async register(registerDto: RegisterDto): Promise<User> {
    // 检查手机号是否已存在
    const existing = await this.userRepository.findOne({
      where: { phone: registerDto.phone },
    });
    if (existing) {
      throw new ConflictException('该手机号已注册');
    }

    // 检查协议是否同意
    if (!registerDto.isAgreementAccepted) {
      throw new BadRequestException('请同意用户协议');
    }

    // 创建用户（密码会在 entity 中自动加密）
    const user = this.userRepository.create({
      phone: registerDto.phone,
      password: registerDto.password,
      nickname: registerDto.nickname,
      avatar: registerDto.avatar || '',
    });

    await this.userRepository.save(user);
    return user;
  }

  /**
   * 用户登录
   */
  async login(loginDto: LoginDto): Promise<{ user: User; token: string }> {
    const user = await this.userRepository.findOne({
      where: { phone: loginDto.phone },
    });

    if (!user) {
      throw new NotFoundException('用户不存在');
    }

    const isValid = await user.validatePassword(loginDto.password);
    if (!isValid) {
      throw new BadRequestException('密码错误');
    }

    // 生成简单的 token（实际项目中建议使用 JWT）
    const token = Buffer.from(JSON.stringify({
      id: user.id,
      phone: user.phone,
      exp: Date.now() + 7 * 24 * 60 * 60 * 1000, // 7 天
    })).toString('base64');

    return { user, token };
  }

  /**
   * 根据手机号查找用户
   */
  async findByPhone(phone: string): Promise<User | null> {
    return this.userRepository.findOne({ where: { phone } });
  }

  /**
   * 根据邮箱查找用户
   */
  async findByEmail(email: string): Promise<User | null> {
    return this.userRepository.findOne({ where: { email } });
  }

  async addDeposit(id: number, amount: number): Promise<User> {
    await this.userRepository.increment({ id }, 'deposit', amount);
    return this.findOne(id);
  }

  async updateRating(id: number, rating: number): Promise<User> {
    const user = await this.findOne(id);
    const newScore = Math.round((user.creditScore + rating) / 2);
    await this.userRepository.update(id, { creditScore: newScore });
    return this.findOne(id);
  }
}
