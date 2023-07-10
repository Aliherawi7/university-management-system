package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record IdentificationDTO(

        @NotNull(message = "نمبر عمومی نباید خالی باشد")
        @Positive(message = "نمبر عمومی نباید کوچکتر از صفر باشد")
        Long nationalId,
        @NotNull(message = "جلد نباید خالی باشد")
        @Positive(message = "جلد نباید کوچکتر از صفر باشد")
        Long caseNumber,
        @NotNull(message = "جلد نباید خالی باشد")
        @Positive(message = "شماره صفحه نباید کوچکتر از صفر باشد")
        Long pageNumber,
        @NotNull(message = "شماره ثبت نباید خالی باشد")
        @Positive(message = "شماره ثبت نباید کوچکتر از صفر باشد")
        Long registrationNumber
) {
}
