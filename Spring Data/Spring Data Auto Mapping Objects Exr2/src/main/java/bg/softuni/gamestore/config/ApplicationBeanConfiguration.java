package bg.softuni.gamestore.config;

import bg.softuni.gamestore.models.dto.GameAddDto;
import bg.softuni.gamestore.models.entities.Game;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .typeMap(GameAddDto.class, Game.class)
                .addMappings(mapper ->
                        mapper.map(GameAddDto::getTrailer, Game::setTrailerId));

        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> context) {
                return context.getSource() == null
                        ? LocalDate.now()
                        : LocalDate.parse(context.getSource(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };

        modelMapper.addConverter(localDateConverter);

        return modelMapper;
    }
}
