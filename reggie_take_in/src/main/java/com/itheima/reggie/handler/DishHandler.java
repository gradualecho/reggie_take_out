package com.itheima.reggie.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.DsihFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishHandler {

    @Autowired
    private DishService dishService;
    @Autowired
    private DsihFlavorService dsihFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){

        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(name),Dish::getName,name);
        wrapper.orderByAsc(Dish::getSort);
        dishService.page(dishPage, wrapper);
//
//        Page<DishDto> dtoPage = new Page<>(page, pageSize);
//        BeanUtils.copyProperties(dishPage,dtoPage,"records");
//
//        List<Dish> records = dishPage.getRecords();
//
//        List<DishDto> record=records.stream().map((item)->{
//            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(item,dishDto);
//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            String name1 = category.getName();
//            dishDto.setCategoryName(name1);
//            return dishDto;
//        }).collect(Collectors.toList());
//        dtoPage.setRecords(record);
        return R.success(dishPage);
    }

    /**
     * 菜品添加
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        dishService.save(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dish.getId());
            return item;
        }).collect(Collectors.toList());

        dsihFlavorService.saveBatch(flavors);
        return R.success("添加成功");
    }

    /**
     * 菜品回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> back(@PathVariable long id) {

        Dish byId = dishService.getById(id);

        LambdaQueryWrapper<DishFlavor> drapper = new LambdaQueryWrapper<>();
        drapper.eq(DishFlavor::getDishId, byId.getId());
        List<DishFlavor> list = dsihFlavorService.list(drapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId, dishDto);
        dishDto.setFlavors(list);
        return R.success(dishDto);

    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> put(@RequestBody DishDto dishDto){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        dishService.updateById(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dish.getId());
            return item;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        dsihFlavorService.remove(wrapper);
        dsihFlavorService.saveBatch(flavors);
        return R.success("修改成功");
    }
}
