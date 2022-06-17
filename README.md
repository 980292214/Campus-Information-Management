### 1.项目说明：

使用技术：SpringBoot、Mybatis-Plus、MySQL、Maven、Vue、Axios等

项目描述：能够对校园的相关信息进行管理，如年级、班级、教师、学生信息等，除了基本的crud功能，还支持修改密码。项目的用户身份有：学生、教师、管理员。

项目亮点：

- 采用前后端分离方式开发，并统一了后端返回结果格式
- 用户登录时使用 token 来做身份验证
- 登录密码使用MD5进行加密
- 整合阿里云OSS，实现了文件上传功能
- 根据用户身份做了权限管理，不同的身份跳转到不同的页面

### 2.部分业务流程

#### 	2.1 获取验证码

#### 	2.2 登录验证（先画流程图分析）

​			1）获取前端传来的 LoginForm （form表单）

​			2）校验验证码是否正确

​			3）若正确，则从 session 域中移除现有验证码，

​			4）然后根据登陆表单内的用户类型（3种）来分类进行校验，

​				（就是根据用户类型找到的对应的表，然后检查用户名和密码是否和数据库内的一致）

​			5）若校验通过，则把用户的类型和用户id转换成一个密文,以token的形式向客户端反馈

#### 	2.3 跳转到首页

​			1）从请求头里获取前端传来的 token

​			2）判断 token 是否已过期

​			3）若没过期，则从 token 中解析出 用户id 和用户类型

​			4）然后根据 用户类型 去数据库里使用 用户id 来查找对应的表

​			5）成功找到后，把 用户类型 和 查询到的用户信息 返回给前端（通过map→Result）

#### 	2.4 模糊查询等级

​			1）获取前端传来的 页码数、页大小、名称

​			2）new 一个 Page 对象，页码数和页大小是构造方法参数

​			3）调用 服务层，page 对象和名称作为方法的参数

​				3.1）new QueryWrapper

​				3.2）然后判断 名称是否为空

​				3.3）不为空则（模糊）查询数据库。

​				3.4）最后返回 page

​			4）封装Result对象并返回。//return Result.ok(pageRs);

#### 	2.5 添加/更新年级信息

#### 	2.6 批量删除年级信息

#### 2.7 获取所有的班级列表

#### 2.8 分页模糊查询班级（年级、班级名）

#### 2.9 批量删除班级信息

2.10 学生管理条件查询

#### 2.11 上传头像

​			1）获取前端传来的 头像 文件

​			2）使用 uuid 重命名

​			3）保存文件，将文件发送到第三方/独立的图片服务器上（这里是保存到本地）

​			4）返回图片的路径给前端

2.12 学生管理添加/修改；批量删除

#### 2.13 修改密码

​			1）从请求头里获取前端传来的 token，获取旧、新密码

​			2）判断 token 是否已过期

​			3）若没过期，则从 token 中解析出 用户id 和用户类型

​			4）然后使用MD5对旧、新密码加密，用于验证

​			4）然后根据 用户类型 去数据库里使用 用户id 和密码来查找对应的表

​			5）成功找到后，更新密码



已更新：登录时也可以支持学号

已更新：上传文件整合阿里云oss