/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.jpetstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperTestContext.class)
@Transactional
class DatabaseViewerTest {

  @Autowired
  private AccountMapper accountMapper;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private ItemMapper itemMapper;

  @Autowired
  private OrderMapper orderMapper;

  @Test
  void viewAllData() {
    System.out.println("========================================");
    System.out.println("          数据库数据查看器");
    System.out.println("========================================");

    printAccounts();
    printCategories();
    printProducts();
    printItems();

    System.out.println("========================================");
    System.out.println("          数据查看完成");
    System.out.println("========================================");
  }

  void printAccounts() {
    System.out.println("\n【用户账户】");
    System.out.println("----------------------------------------");
    List<String> usernames = List.of("j2ee", "ACID");
    for (String username : usernames) {
      Account account = accountMapper.getAccountByUsername(username);
      if (account != null) {
        System.out.printf("用户名: %-10s 密码: %-10s 邮箱: %s%n",
            account.getUsername(), "******", account.getEmail());
        System.out.printf("  姓名: %s %s%n", account.getFirstName(), account.getLastName());
      }
    }
  }

  void printCategories() {
    System.out.println("\n【商品分类】");
    System.out.println("----------------------------------------");
    List<Category> categories = categoryMapper.getCategoryList();
    for (Category category : categories) {
      System.out.printf("分类ID: %-10s 名称: %s%n",
          category.getCategoryId(), category.getName());
    }
  }

  void printProducts() {
    System.out.println("\n【商品列表】");
    System.out.println("----------------------------------------");
    List<Category> categories = categoryMapper.getCategoryList();
    for (Category category : categories) {
      System.out.printf("%n--- %s ---%n", category.getName());
      List<Product> products = productMapper.getProductListByCategory(category.getCategoryId());
      for (Product product : products) {
        System.out.printf("  商品ID: %-10s 名称: %s%n",
            product.getProductId(), product.getName());
      }
    }
  }

  void printItems() {
    System.out.println("\n【商品详情】（前10个）");
    System.out.println("----------------------------------------");
    List<Category> categories = categoryMapper.getCategoryList();
    int count = 0;
    for (Category category : categories) {
      List<Product> products = productMapper.getProductListByCategory(category.getCategoryId());
      for (Product product : products) {
        List<Item> items = itemMapper.getItemListByProduct(product.getProductId());
        for (Item item : items) {
          if (count < 10) {
            System.out.printf("商品ID: %-10s 属性: %-15s 价格: %6.2f 库存: %d%n",
                item.getItemId(), item.getAttribute1(), item.getListPrice(), item.getQuantity());
            count++;
          }
        }
      }
    }
  }
}
