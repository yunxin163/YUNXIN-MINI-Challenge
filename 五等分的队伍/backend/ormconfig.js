module.exports = {
  type: "mysql",
  host: "localhost",
  port: 3306,
  username: "root",
  password: "3802985434",
  database: "livechat",
  synchronize: true,
  entities: [
    "src/entity/*.ts"
  ],
  migrations: ["src/migration/*.ts"],
  cli: {
    migrationsDir: "migration"
  }
}

/* 
  synchronize: true 后遇到 
  TypeError: Cannot read property 'COLLATION' of undefined
  原因：数据库名存在大写字母，改成下划线形式即解决
*/