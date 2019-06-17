import { Unique ,Entity, PrimaryGeneratedColumn, CreateDateColumn, UpdateDateColumn, Column } from 'typeorm';

@Entity()
export class BaseEntity {
  // id
  @PrimaryGeneratedColumn()
  id: number;

  // 是否删除
  @Column({
    default: false,
    select: false
  })
  is_delete: boolean;

  // 创建日期
  @CreateDateColumn()
  create_at: Date;

  // 更新日期
  @UpdateDateColumn({
    select: false
  })
  update_at: Date;
}