package com.unitech.backend.transfer.controller;

import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.request.TransferRequestDTO;
import com.unitech.backend.transfer.service.TransferService;
import com.unitech.backend.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
@RequestMapping(value = {"/transfer"})
public class TransferController {

    private final TransferService transferService;

    @PostMapping(value = "/makeTransfer")
    public ApiResponseDTO transfer(@RequestBody TransferRequestDTO requestDTO) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return transferService.transfer(currentUser.getId(), requestDTO.getFromIdentifier(), requestDTO.getToIdentifier(), requestDTO.getAmount());
        }
        catch (Exception e){
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }

}
