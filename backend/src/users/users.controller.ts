import { Controller, Get, Post, Body, Param, ParseIntPipe, HttpCode, HttpStatus } from '@nestjs/common';
import { UsersService } from './users.service';
import { User } from './user.entity';
import { CreateUserDto, RegisterDto, LoginDto } from './dto/create-user.dto';

@Controller('api/users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Get(':id')
  findOne(@Param('id', ParseIntPipe) id: number): Promise<User> {
    return this.usersService.findOne(id);
  }

  @Get()
  findAll(): Promise<User[]> {
    return this.usersService.findAll();
  }

  @Post()
  create(@Body() createUserDto: CreateUserDto): Promise<User> {
    return this.usersService.create(createUserDto);
  }

  /**
   * 用户注册
   */
  @Post('register')
  @HttpCode(HttpStatus.CREATED)
  register(@Body() registerDto: RegisterDto): Promise<User> {
    return this.usersService.register(registerDto);
  }

  /**
   * 用户登录
   */
  @Post('login')
  @HttpCode(HttpStatus.OK)
  login(@Body() loginDto: LoginDto): Promise<{ user: User; token: string }> {
    return this.usersService.login(loginDto);
  }

  /**
   * 检查手机号是否可用
   */
  @Get('check-phone/:phone')
  async checkPhone(@Param('phone') phone: string): Promise<{ available: boolean }> {
    const user = await this.usersService.findByPhone(phone);
    return { available: !user };
  }

  @Post(':id/deposit')
  addDeposit(@Param('id', ParseIntPipe) id: number, @Body() body: any): Promise<User> {
    return this.usersService.addDeposit(id, body.amount);
  }

  @Post(':id/rating')
  updateRating(@Param('id', ParseIntPipe) id: number, @Body() body: any): Promise<User> {
    return this.usersService.updateRating(id, body.rating);
  }
}
