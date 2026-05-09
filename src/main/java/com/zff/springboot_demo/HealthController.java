package com.zff.springboot_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统健康检查接口
 */
@RestController
public class HealthController {

    /**
     * 获取服务健康状态
     * @return 健康状态信息
     */
    @GetMapping("/api/health")
    public Result<HealthData> health() {
        HealthData data = new HealthData("ok",System.currentTimeMillis());
        return Result.success("健康检查成功",data);
    }

    static class HealthData {
        private String status;
        private Long timestamp;
        public HealthData(String status, Long timestamp) {
            this.status = status;
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public Long getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
