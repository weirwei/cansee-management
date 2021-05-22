# cansee-management 日志管理系统

## 开发日志
### 数据库设计

### 登录注册

### 页面展示
- 团队管理
    - [x] 展示  
    - [x] 搜索
    - [x] 创建团队
    - [x] 注销
- 成员管理
    - [x] 展示
    - [x] 搜索
    - [x] 添加成员
    - [x] 权限分配
    - [x] 移除
### 日志展示

### 项目绑定


## TODO
- [ ] 团队展示的分页优化

## 接口整理
组织相关
- `GET /cansee/organization/org`  分页搜索组织
- `POST /cansee/organization/org`  创建组织
- `DELETE /cansee/organization/org`  删除组织
  
成员相关
- `GET /cansee/organization/{orgId}/menber`  分页搜索组织下成员
- `POST /cansee/organization/{orgId}/member/{uid}`  新增用户
- `DELETE /cansee/organization/{orgId}/member/{uid}`  删除成员
- `PUT /cansee/organization/{orgId}/member/{uid}`  修改成员权限

项目相关
- `GET /cansee/organization/{orgId}/proj`  分页搜索组织下项目
- `POST /cansee/organization/{orgId}/proj/{projId}`  新增项目
- `DELETE /cansee/organization/{orgId}/proj/{projId}`  删除项目
- `GET /cansee/organization/{orgId}/proj/{projId}`  获取项目配置信息

日志相关
- `GET /cansee/organization/{orgId}/proj/{projId}/log`  分页搜索组织下日志
- `DELETE /cansee/organization/{orgId}/proj/{projId}/log/{logId}`  删除日志
- `PUT /cansee/organization/{orgId}/proj/{projId}/log/{logId}`  标记解决
