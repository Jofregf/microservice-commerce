package app.portfoliojofregf.vercel.products_service.service;

import app.portfoliojofregf.vercel.products_service.exceptions.ProductNotFoundException;
import app.portfoliojofregf.vercel.products_service.model.dtos.ProductRequest;
import app.portfoliojofregf.vercel.products_service.model.dtos.ProductResponse;
import app.portfoliojofregf.vercel.products_service.model.entity.Product;
import app.portfoliojofregf.vercel.products_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest) {
        var product = Product.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .status(productRequest.getStatus())
                .build();

        productRepository.save(product);

        log.info("Product added: {}", product);
    }

    public List<ProductResponse> getAllProducts(){
        var products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .build();
    }

    public ProductResponse fingById(Long id){
        Optional<Product> productOpt = productRepository.findById(id);
        if(productOpt.isPresent()){
            Product product = productOpt.get();
            return mapToProductResponse(product);
        } else {
            throw new ProductNotFoundException("Product not found with Id: " + id);
        }
    }
    public void deleteProduct(Long id) {

    }
}
