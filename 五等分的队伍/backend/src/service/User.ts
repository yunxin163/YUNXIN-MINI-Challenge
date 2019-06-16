import { getRepository } from 'typeorm'
import { User } from '../entity/User'

export default class RoleService {
  // /**
  //  * 根据 id 寻找用户
  //  */
  // public static async findUserById(id: number) {
  //   return await getRepository(User)
  //   .createQueryBuilder('user')
  //   .where('id = :id', { id })
  //   .getOne();
  // }

  // /**
  //  * 根据 username 寻找用户
  //  */
  // public static async findUserByUsername(username: string) {
  //   return await getRepository(User)
  //   .createQueryBuilder('user')
  //   .where('username = :username', { username })
  //   .getOne();
  // }  
}