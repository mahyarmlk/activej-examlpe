package org.example.activej.modules.vehicles.dto;

import com.dslplatform.json.CompiledJson;

@CompiledJson
public class YearDto {
    public final String year;

    public YearDto(String year) {
        this.year = year;
    }
}
