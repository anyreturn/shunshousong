import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  
  // 启用 CORS
  app.enableCors({
    origin: '*',
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS',
    credentials: true,
  });
  
  // 全局验证管道
  app.useGlobalPipes(new ValidationPipe({ transform: true }));
  
  const port = process.env.PORT || 4000;
  await app.listen(port);
  console.log(`🚀 顺手送 API 服务运行在 http://localhost:${port}`);
  console.log(`📡 API 文档：http://localhost:${port}/api`);
}
bootstrap();
