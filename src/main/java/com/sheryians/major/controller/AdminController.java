package com.sheryians.major.controller;

import com.sheryians.major.dto.productDTO;
import com.sheryians.major.model.Category;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    public static String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/productImages";
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    //CATEGORY SECTION
    @GetMapping("/admin/categories")
    public String getCat(Model model)
    {
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model)
    {
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }
    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute ("category")Category category)
    {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id){
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id,Model model)
    {
        Optional<Category> category=categoryService.getCategoryById(id);
        if(category.isPresent())
        {
            model.addAttribute("category",category.get());
            return "categoriesAdd";
        }
        else{
            return "404";
        }

    }


    //PRODUCT SECTION
    @GetMapping("admin/products")
    public String products(Model model)
    {
        model.addAttribute("products" , productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model)
    {
        model.addAttribute("productDTO",new productDTO());
        model.addAttribute("categories",categoryService.getAllCategory());
        return "productsAdd";

    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO")productDTO ProductDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imgName)throws IOException
    {
        //image ko object mein wrap up nhi kiya h aur jab request jayegi toh usmeimage bhi include
        //hogi isleie wo saari properties jo onject mein nhi hoti usko requestparam mein bhejte h
        Product product=new Product();
        product.setId(ProductDTO.getId());
        product.setName(ProductDTO.getName());
        product.setCategory(categoryService.getCategoryById(ProductDTO.getCategoryId()).get());
        product.setPrice(ProductDTO.getPrice());
        product.setWeight(ProductDTO.getWeight());
        product.setDescription(ProductDTO.getDescription());
        String imageUUID;
        if(!file.isEmpty())
        {
            imageUUID= file.getOriginalFilename();
            Path fileNameAndPath= Paths.get(uploadDir,imageUUID);
            Files.write(fileNameAndPath,file.getBytes());

        }
        else {
            imageUUID=imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id)
    {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id,Model model)
    {
        Product product= productService.getProductById(id).get();
        productDTO ProductDTO=new productDTO();
        ProductDTO.setId(product.getId());
        ProductDTO.setName(product.getName());
        ProductDTO.setPrice(product.getPrice());
        ProductDTO.setDescription(product.getDescription());
        ProductDTO.setWeight(product.getWeight());
        ProductDTO.setImageName(product.getImageName());
        ProductDTO.setCategoryId(product.getCategory().getId());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",ProductDTO);

        return "productsAdd";
    }




}
