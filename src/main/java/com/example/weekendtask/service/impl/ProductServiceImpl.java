package com.example.weekendtask.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.weekendtask.dao.ProductRepo;
import com.example.weekendtask.entity.Product;
import com.example.weekendtask.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";


	@Override
	public Iterable<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Product addProduct(String productForm, MultipartFile productImage) throws JsonMappingException, JsonProcessingException {
		Date date = new Date();
		Product product = new ObjectMapper().readValue(productForm, Product.class);
		String fileExtension = productImage.getContentType().split("/")[1];
		String newFileName = "ProdImage-" + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		
		try {
			Files.copy(productImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String image = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/download/").path(fileName).toUriString();
		
		product.setId(0);
		product.setProductImage(image);
		
		return productRepo.save(product);
	}

	@Override
	public Product updateProduct(String productForm, MultipartFile productImage, int productId)
			throws JsonMappingException, JsonProcessingException {
		Date date = new Date();	
		Product product = new ObjectMapper().readValue(productForm, Product.class);
		String fileExtension = productImage.getContentType().split("/")[1];
		String newFileName = "ProdImage-" + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		
		try {
			Files.copy(productImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Product findProduct = productRepo.findById(productId).get();
		String image = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/download/").path(fileName).toUriString();
		
		product.setId(productId);
		product.setProductImage(image);
		
		return productRepo.save(product);
	}

	@Override
	public void deleteProduct(int productId) {
		Product findProduct = productRepo.findById(productId).get();
		productRepo.deleteById(productId);
	}

	@Override
	public Optional<Product> getProductById(int productId) {
		return productRepo.findById(productId);
	}

}
