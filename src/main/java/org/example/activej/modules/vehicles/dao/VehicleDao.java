package org.example.activej.modules.vehicles.dao;

import io.activej.promise.Promise;
import lombok.extern.slf4j.Slf4j;
import org.example.activej.modules.vehicles.dto.YearDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


@Slf4j
public class VehicleDao {
    private final DataSource dataSource;
    private final Executor executor;

    public VehicleDao(Executor executor, DataSource dataSource) {
        this.dataSource = dataSource;
        this.executor = executor;
    }

    public Promise<List<YearDto>> getYears() {
        return Promise.ofBlocking(executor, () -> {
            try (Connection connection = dataSource.getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet rs = statement.executeQuery("SELECT year FROM years ORDER BY year DESC")) {
                        List<YearDto> years = new ArrayList<>();

                        while (rs.next()) {
                            years.add(new YearDto(rs.getString(1)));
                        }

                        return years;
                    }
                }
            }
        });
    }
}
