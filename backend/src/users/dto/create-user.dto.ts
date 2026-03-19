import { IsString, IsEmail, IsOptional, MinLength, Matches, IsBoolean } from 'class-validator';

export class CreateUserDto {
  @IsString()
  @IsOptional()
  openid?: string;

  @IsString()
  @Matches(/^1[3-9]\d{9}$/, { message: '手机号格式不正确' })
  @IsOptional()
  phone?: string;

  @IsEmail({}, { message: '邮箱格式不正确' })
  @IsOptional()
  email?: string;

  @IsString()
  @MinLength(6, { message: '密码至少需要 6 位' })
  password: string;

  @IsString()
  @MinLength(2, { message: '昵称至少需要 2 位' })
  nickname: string;

  @IsString()
  @IsOptional()
  avatar?: string;

  @IsString()
  @IsOptional()
  realName?: string;

  @IsString()
  @IsOptional()
  idCard?: string;

  @IsBoolean()
  @IsOptional()
  isAgreementAccepted?: boolean;
}

export class RegisterDto {
  @IsString()
  @Matches(/^1[3-9]\d{9}$/, { message: '手机号格式不正确' })
  phone: string;

  @IsString()
  @MinLength(6, { message: '密码至少需要 6 位' })
  password: string;

  @IsString()
  @MinLength(2, { message: '昵称至少需要 2 位' })
  nickname: string;

  @IsString()
  @IsOptional()
  avatar?: string;

  @IsBoolean()
  isAgreementAccepted: boolean;
}

export class LoginDto {
  @IsString()
  @Matches(/^1[3-9]\d{9}$/, { message: '手机号格式不正确' })
  phone: string;

  @IsString()
  password: string;
}
