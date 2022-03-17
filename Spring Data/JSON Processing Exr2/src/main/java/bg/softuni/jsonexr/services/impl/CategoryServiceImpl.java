package bg.softuni.jsonexr.services.impl;

import bg.softuni.jsonexr.constants.GlobalConstants;
import bg.softuni.jsonexr.models.Category;
import bg.softuni.jsonexr.models.dtos.CategorySeedDto;
import bg.softuni.jsonexr.repositories.CategoryRepository;
import bg.softuni.jsonexr.services.CategoryService;
import bg.softuni.jsonexr.utils.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static String CATEGORIES_FILE_NAME = "categories.json";

    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(Gson gson, ValidationUtil validationUtil, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0){
            return;
        }

        String fileContent = Files
                .readString(Path.of(GlobalConstants.RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos = gson
                .fromJson(fileContent,CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(CategorySeedDto -> modelMapper.map(CategorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }
}
