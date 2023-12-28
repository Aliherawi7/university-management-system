package com.mycompany.umsapi.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

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
