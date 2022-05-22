package com.itheima.reggie.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryHandler {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        Page<Category> page1 = categoryService.page(categoryPage, wrapper);

        return R.success(categoryPage);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam long ids){
        categoryService.removeById(ids);
        return R.success("删除成功");
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> put(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 添加套餐/菜品
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Integer type){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type);
        wrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
