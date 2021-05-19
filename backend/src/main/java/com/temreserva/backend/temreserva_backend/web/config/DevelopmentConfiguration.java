package com.temreserva.backend.temreserva_backend.web.config;

import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.MailTemplateRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantDateTimeRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.SegmentRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.data.entity.Address;
import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.Image;
import com.temreserva.backend.temreserva_backend.data.entity.MailTemplate;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.RestaurantDateTime;
import com.temreserva.backend.temreserva_backend.data.entity.Segment;
import com.temreserva.backend.temreserva_backend.data.entity.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Configuration
@Profile("development")
public class DevelopmentConfiguration {
    private String template = "<!DOCTYPE htmlPUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns:th='http://www.thymeleaf.org' xmlns='http://www.w3.org/1999/xhtml'><head><title>Sua reserva foi confirmada!</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /><meta name='viewport' content='width=device-width, initial-scale=1.0' /><link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css' /><!-- use the font --><style>body {font-family: 'Roboto', sans-serif;font-size: 48px;}</style></head><body><h2>SUA RESERVA ESTÁ CONFIRMADA!!!</h2></body></html>";

    // INSERÇÕES PARA BANCO EM MEMÓRIA
    @Bean
    public CommandLineRunner createDataForTesting(@Autowired SegmentRepository segmentRepository,
            @Autowired UserRepository userRepository, @Autowired RestaurantRepository restaurantRepository,
            @Autowired CredentialRepository credentialRepository, @Autowired AddressRepository addressRepository,
            @Autowired ImageRepository imageRepository, @Autowired ImageBusiness imageBusiness,
            @Autowired MailTemplateRepository mailTemplateRepository, @Autowired RestaurantDateTimeRepository restaurantDateTimeRepository) {
        return args -> {
            System.out.println("Ambiente de desenvolvimento...");

            mailTemplateRepository
                    .save(MailTemplate.builder().description("reserve_success").html(template).active(true).build());

            List<Segment> lstSegment = new ArrayList<Segment>();
            lstSegment.add(Segment.builder().description("Japonês").build());
            lstSegment.add(Segment.builder().description("Mexicana").build());
            lstSegment.add(Segment.builder().description("Italiana").build());
            segmentRepository.saveAll(lstSegment);

            Credential cred1 = Credential.builder().email("teste@email.com").password("123").build();
            cred1 = credentialRepository.save(cred1);
            User user1 = User.builder().credential(cred1).cpf("53415927865").name("Ester Goulveia").phoneNumber("11984100125")
            .birthDate(LocalDate.of(2000, 5, 30)).build();
            userRepository.save(user1);

            JSONParser jsonParser = new JSONParser();
            JSONArray arr = (JSONArray) jsonParser.parse(new FileReader("src/main/resources/restaurants-mock.json"));

            for (Object o : arr) {
                JSONObject res = (JSONObject) o;

                String restaurantName = (String) res.get("restaurantName");
                String phoneNumber = (String) res.get("phoneNumber");
                String email = (String) res.get("email");
                String password = (String) res.get("password");
                String cnpj = (String) res.get("cnpj");
                String description = (String) res.get("description");
                // String openDaysOfWeek = (String) res.get("openDaysOfWeek");

                // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                // long ot = sdf.parse((String) res.get("openingTime")).getTime();
                // Time openingTime = new Time(ot);
                // long ct = sdf.parse((String) res.get("closingTime")).getTime();
                // Time closingTime = new Time(ct);

                String cleaning = res.get("cleaning").toString();
                String payment = res.get("payment").toString();
                int spacingOfTables = Integer.parseInt(res.get("spacingOfTables").toString());
                int maxNumberOfPeople = Integer.parseInt(res.get("maxNumberOfPeople").toString());

                JSONObject address = (JSONObject) res.get("address");
                String cep = (String) address.get("cep");
                String district = (String) address.get("district");
                String ad = (String) address.get("address");
                String complement = (String) address.get("complement");
                String locality = (String) address.get("locality");
                int restaurantNumber = Integer.parseInt(address.get("restaurantNumber").toString());
                String zone = (String) address.get("zone");
                String uf = (String) address.get("uf");

                Credential cred = Credential.builder().email(email).password(password).build();
                cred = credentialRepository.save(cred);

                Restaurant restaurant = Restaurant.builder().credential(cred).restaurantName(restaurantName)
                        .phoneNumber(phoneNumber).cnpj(cnpj).description(description).cleaning(cleaning)
                        .payment(payment).spacingOfTables(spacingOfTables).maxNumberOfPeople(maxNumberOfPeople).build();

                restaurant = restaurantRepository.save(restaurant);

                Address addr = Address.builder().restaurant(restaurant).address(ad).cep(cep).district(district)
                        .complement(complement).locality(locality).restaurantNumber(restaurantNumber).zone(zone).uf(uf)
                        .build();
                addressRepository.save(addr);

                int imageNumber = (int) (Math.random() * ((8 - 1) + 1)) + 1;

                MultipartFile file = new MockMultipartFile("Painting" + imageNumber + ".png",
                        new FileInputStream(new File("src/main/resources/images/Painting" + imageNumber + ".png")));
                Image image = imageBusiness.buildImage(file, restaurant.getId(), true, true);
                imageRepository.save(image);

                for (int i = 0; i < 4; i++) {
                    MultipartFile fileTest = new MockMultipartFile("Painting" + imageNumber + ".png",
                            new FileInputStream(new File("src/main/resources/images/Painting" + imageNumber + ".png")));
                    Image imageTest = imageBusiness.buildImage(fileTest, restaurant.getId(), false, true);
                    imageRepository.save(imageTest);
                }

                JSONArray restaurantDateTime = (JSONArray) res.get("restaurantDateTime");
                for (Object rDateTime : restaurantDateTime) {
                        JSONObject restTime = (JSONObject) rDateTime;

                        String day = (String) restTime.get("day");
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        long ot = sdf.parse((String) restTime.get("openingTime")).getTime();
                        Time openingTime = new Time(ot);
                        long ct = sdf.parse((String) restTime.get("closingTime")).getTime();
                        Time closingTime = new Time(ct);

                        RestaurantDateTime resDateTime = RestaurantDateTime.builder().restaurant(restaurant).day(day)
                        .openingTime(openingTime).closingTime(closingTime).build();
                        restaurantDateTimeRepository.save(resDateTime);
                }
            }
        };
    }
}
