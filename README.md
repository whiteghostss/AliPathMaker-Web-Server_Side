## 运行

### 推荐方式（Docker）：

```
bash deploy.sh
# 可能出现 python:3.10-slim拉取的网络问题，可以先docker pull python:3.10-slim再运行
bash deploy.sh,同时下载可能速度较慢，耐心等待即可
```

## 常用调试命令

- 查看容器日志（实时输出后端 print/logging）：

  ```
  docker logs -f alipathmarker-backend
  ```

- 进入容器内部（需要 Dockerfile 已安装 bash）：

  ```
  docker exec -it alipathmarker-backend bash
  ```

- 查看解压后的上传目录（容器内操作，sessionId 替换为实际值）：

  ```
  cd uploads/你的sessionId
  ls -l
  ```

- 停止并删除容器：
  ```
  docker rm -f alipathmarker-backend
  ```
1. 排期导出过滤器 - 最复杂的方法
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/campaigngroup/spi/export/filter/DefaultBizScheduleExportFilterSpiImpl.java
方法名: filterCampaign()
复杂度: ⭐⭐⭐⭐⭐ (最高)
路径数量预估: 30-50个路径
复杂特征:
多个 switch-case 语句嵌套
复杂的条件分支判断
多层嵌套的 if-else 结构
大量的业务逻辑分支
2. 创意验证方法
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/creative/spi/impl/validate/DefaultCreativeValidateSpiImpl.java
方法名: checkCreative() 和 validityCheck()
复杂度: ⭐⭐⭐⭐
路径数量预估: 20-30个路径
复杂特征:
多个元素类型验证
复杂的模板验证逻辑
多种创意元素处理分支
3. 售卖分组预估处理
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/salegroup/spi/impl/estimate/productline/DefaultBizSaleGroupEstimateAbilitySpiImpl.java
方法名: processFinalValue()
复杂度: ⭐⭐⭐⭐
路径数量预估: 20-25个路径
复杂特征:
多种交付目标处理
复杂的开关逻辑判断
多层级的数据处理
4. 计划模型获取方法
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/campaign/atomability/BrandCampaignBaseInitForAddCampaignAbility.java
方法名: getCampaignModel()
复杂度: ⭐⭐⭐⭐
路径数量预估: 20-25个路径
复杂特征:
多个媒体域判断
复杂的投放方式逻辑
跨域场景处理
5. 跨域子计划初始化
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/campaign/atomability/subcampaign/BrandSelfCampaignBaseInitForCrossSplitSubCampaignAbility.java
方法名: getCampaignModel()
复杂度: ⭐⭐⭐⭐
路径数量预估: 20-25个路径
复杂特征:
多种跨域场景处理
复杂的媒体域判断
投放方式组合逻辑
6. 计划拆分验证
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/campaign/spi/impl/CrossBizCampaignSplitSpiImpl.java
方法名: validateCrossSplitRule()
复杂度: ⭐⭐⭐
路径数量预估: 15-20个路径
复杂特征:
多种产品类型验证
复杂的拆分规则判断
7. 创意审核步骤判断
文件位置: src/main/java/com/taobao/ad/brand/bp/domain/creative/spi/impl/mediascope/DefaultCreativeSpiImpl.java
方法名: getCreativeAuditStep()
复杂度: ⭐⭐⭐
路径数量预估: 15-20个路径
复杂特征:
多种审核状态判断
复杂的审核流程逻辑
