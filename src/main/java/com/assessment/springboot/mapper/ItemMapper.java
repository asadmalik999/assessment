package com.assessment.springboot.mapper;

import com.assessment.springboot.dto.ItemDto;
import com.assessment.springboot.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "category.name", target = "categoryName") // Ensure the source path is correct
    ItemDto itemToItemResponse(Item item);
}
