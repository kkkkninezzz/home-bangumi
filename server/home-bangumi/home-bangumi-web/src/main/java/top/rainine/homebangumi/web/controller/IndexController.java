package top.rainine.homebangumi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @authoer rainine
 * @date 2024/5/3 11:18
 * @desc 默认页视图
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "web-ui/index.html"; // 返回web-ui默认地址
    }
}
