import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getHello(): string {
    return '👋 欢迎来到顺手送 API 服务！\n\n' +
           '📚 API 端点:\n' +
           '  GET  /api/orders      - 获取订单列表\n' +
           '  GET  /api/orders/:id  - 获取订单详情\n' +
           '  POST /api/orders      - 创建订单\n' +
           '  POST /api/orders/:id/accept - 接单\n' +
           '  POST /api/orders/:id/complete - 完成订单\n' +
           '  GET  /api/users/:id   - 获取用户信息\n' +
           '  POST /api/users       - 创建用户\n' +
           '  GET  /health          - 健康检查';
  }

  @Get('health')
  health(): { status: string; timestamp: string } {
    return { status: 'OK', timestamp: new Date().toISOString() };
  }
}
