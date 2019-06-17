import { Context } from 'koa';

export default async(ctx: Context, next) => {
  ctx.error = ({ data, msg }) => {
    ctx.body = { code: -200, msg, data };
  }
  
  ctx.success = ({ data, msg, pageable }) => {
    ctx.body = { code: 200, msg, data, pageable };
  }
  await next()
}