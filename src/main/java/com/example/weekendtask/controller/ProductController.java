package com.example.weekendtask.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weekendtask.entity.Product;
import com.example.weekendtask.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";

	
	@GetMapping
	public Iterable<Product> getAllProducts() {
		return productService.getAllProducts();
	}
	
	@GetMapping("/readbyid")
	public Optional<Product> getProductById(@RequestParam int productId){
		return productService.getProductById(productId);
	}

	@PostMapping("/add")
	public Product addProduct(@RequestParam("productForm") String productForm, @RequestParam("productImage") MultipartFile productImage) throws JsonMappingException, JsonProcessingException {
		return productService.addProduct(productForm, productImage);
	}
	
	@PutMapping("/update")
	public Product updateProduct(@RequestParam("productForm") String productForm, @RequestParam("productImage") MultipartFile productImage, @RequestParam int productId) throws JsonMappingException, JsonProcessingException {
		return productService.updateProduct(productForm, productImage, productId);
	}
	
	@DeleteMapping("/delete")
	public void deleteProduct(@RequestParam int productId) {
		productService.deleteProduct(productId);
	}
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName) {
		Path path = Paths.get(uploadPath + fileName);
		Resource resource = null;
		
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		System.out.println("DOWNLOAD");
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"").body(resource);
	}
	
}
