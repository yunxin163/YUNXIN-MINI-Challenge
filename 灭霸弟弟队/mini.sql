/*
 Navicat Premium Data Transfer

 Source Server         : mydb
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : mini

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 15/06/2019 21:14:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for evaluation
-- ----------------------------
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE `evaluation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) NULL DEFAULT NULL,
  `student_id` int(11) NULL DEFAULT NULL,
  `order_id` int(11) NULL DEFAULT NULL,
  `scores` tinyint(1) NULL DEFAULT NULL COMMENT '课程评分，1到5',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `likes` int(11) NULL DEFAULT NULL COMMENT '点赞数',
  `timestamp` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价表，对于每一次家教课程，结束时都有一个学生评价。教师评价，评价的循环评价暂不做' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of evaluation
-- ----------------------------
INSERT INTO `evaluation` VALUES (1, 1, 1, 1, 1, '老师讲得非常好我感觉获益匪浅', 30, '2019-06-02 19:11:53');
INSERT INTO `evaluation` VALUES (2, 1, 1, 2, 5, 'amazing', 10, NULL);
INSERT INTO `evaluation` VALUES (3, 1, 1, 3, 5, 'Again a great teacher', NULL, '2019-06-01 22:46:58');

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `user_id` int(11) NOT NULL,
  `stuff_type` tinyint(1) NULL DEFAULT NULL COMMENT '1代表关注的是student，2代表关注的是teacher，3代表关注的是evaluation',
  `stuff_id` int(12) NULL DEFAULT NULL COMMENT '被关注数据(1/2/3)的id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '意为“我的关注”,用于关注教师，学生或评价,teacher,student都有关注的功能' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (8564848, 2, 541321564);
INSERT INTO `follow` VALUES (8564848, 2, 12365478);
INSERT INTO `follow` VALUES (8564848, 1, 12121212);
INSERT INTO `follow` VALUES (8564848, 3, 1);
INSERT INTO `follow` VALUES (8564848, 3, 2);
INSERT INTO `follow` VALUES (8564848, 3, 3);
INSERT INTO `follow` VALUES (8564848, 1, 123123123);
INSERT INTO `follow` VALUES (8564848, 1, 345634534);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` enum('Chinese','math','English') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_id` int(11) NULL DEFAULT NULL,
  `teacher_id` int(11) NULL DEFAULT NULL,
  `evaluation_id` int(11) NULL DEFAULT NULL COMMENT '评价',
  `start_time` time(0) NULL DEFAULT NULL COMMENT '课程开始时间',
  `end_time` time(0) NULL DEFAULT NULL COMMENT '课程结束时间',
  `price` decimal(4, 2) NULL DEFAULT NULL,
  `order_time` timestamp(6) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '下单时间或修改时间',
  `is_cancel` bit(1) NULL DEFAULT NULL COMMENT '1代表没有取消，0代表课程取消',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'record就是订单记录，记录下每一次的订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 'Chinese', 1, 1, 1, '00:00:00', NULL, 1.11, '2019-06-01 12:10:41.315994', b'1');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL COMMENT '账号，6~10位可供用户自选，主键（不递增，不用UUID）',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码。长度限制在6到12位，业务上限制',
  `account_points` int(11) NULL DEFAULT NULL COMMENT '账户积分，后期可以添加花式功能',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户余额，小数点两位',
  `head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像，存储路径',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` enum('male','female') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别，enum类型，可选male/female',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自我介绍',
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `grade` tinyint(1) NULL DEFAULT NULL COMMENT '年级，一到六',
  `subjects` set('Chinese','math','English') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '科目，set类型，可多选语数外',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号，不可变，11位',
  `wechat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `QQ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市_如果要加这个功能需要业务控制',
  `total` int(11) NULL DEFAULT NULL COMMENT '总课程次数',
  `ave_scores` decimal(3, 2) NULL DEFAULT NULL COMMENT '给出的评分平均值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表，主体用户之一' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (123456, '123456', 50, 150.56, 'C:/Users/Administrator/Desktop/upload_folder/zjl.jpeg', 'admin', 'female', '周杰伦', '快使用双节棍', '台湾小学', 3, 'Chinese,math,English', '18946561234', NULL, NULL, NULL, '台湾', '高雄', 78, 4.62);
INSERT INTO `student` VALUES (8564848, 'zxy15452', 150, 1000.06, NULL, '歌神', 'male', '张学友', '她来听我的演唱会', '杭州实验小学', 2, 'Chinese', '15832146592', NULL, NULL, NULL, '浙江', '杭州', 101, 3.89);
INSERT INTO `student` VALUES (12121212, 'cc121212', 250, 45.56, NULL, '曹哥', 'male', '曹操', '宁我负人，毋人负我。', '杭州第三小学', 1, 'Chinese,math,English', '15212345677', NULL, NULL, NULL, '浙江', '杭州', 26, 3.20);
INSERT INTO `student` VALUES (12345678, 'zal654321', 290, 70.15, NULL, 'anny', 'female', '张安妮', NULL, '杭州第二小学', 6, 'math', '15212345678', NULL, NULL, NULL, '浙江', '杭州', 35, 4.82);
INSERT INTO `student` VALUES (15489564, 'lhy123456', 800, 300.15, NULL, '瓜子儿', 'male', '李洪洋', '我爱吃瓜子儿。', '成都第三小学', 6, 'English', '15112345678', NULL, NULL, NULL, '四川', '成都', 60, 4.60);
INSERT INTO `student` VALUES (123123123, 'pwd1', 500, 59.30, NULL, 'xiaoming', 'male', '杜甫', '我是一个快乐的小学生', '杭州实验小学', 3, 'Chinese,math,English', '13312345678', NULL, NULL, NULL, '浙江', '杭州', 20, 4.20);
INSERT INTO `student` VALUES (234234234, 'xh123456', 480, 100.56, NULL, '小花', 'female', '李清照', '我可真漂亮呀', '杭州实验小学', 3, 'Chinese,math', '13112345678', NULL, NULL, NULL, '浙江', '杭州', 26, 4.10);
INSERT INTO `student` VALUES (345634534, 'db123456', 520, 200.45, NULL, '大白', 'male', '李白', 'I\'m iron man，', '温州实验小学', 5, 'math', '13612345678', NULL, NULL, NULL, '浙江', '温州', 46, 3.56);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(11) NOT NULL COMMENT '同student表',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `account_points` int(11) NULL DEFAULT NULL,
  `balance` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `id_card` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` enum('male','femal') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `birthyear` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `grade` smallint(1) NULL DEFAULT NULL COMMENT '1到5,有些专业五年制',
  `subjects` set('Chinese','math','English') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `total` int(11) NOT NULL COMMENT '总授课次数',
  `ave_scores` decimal(3, 2) NULL DEFAULT NULL COMMENT '课程平均分',
  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主要用户之一\r\n\r\n和学生表类似，但是没有nickname，只有真实姓名，还多了身份证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Chinese', 1, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (2, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'math,English', 1, NULL, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (3, '1', 0, 0, '王富贵', '1', NULL, NULL, NULL, NULL, NULL, 1, 'Chinese,math', 0, 0.00, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (4, '1', 0, 0, '柯学家修改', '33610619901111249X', NULL, NULL, '芭思蔻会返回，白花蛇草水，都看不出快递寄回充不上电科技城拔和肯德基吃', '1990', '北大青鸟', 4, 'math', 0, 0.00, '应用数学', '18769345520', '2314@126.com');

SET FOREIGN_KEY_CHECKS = 1;
