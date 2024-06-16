package mapper;

import model.dto.CustomerDto;
import model.entity.Customer;

public class Mapper {
    public static CustomerDto fromCustomerToCustomerDto(Customer customer){
        if(customer==null){
            return null;
        }
        return CustomerDto.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .id(customer.getId())
                .build();
    }
}
