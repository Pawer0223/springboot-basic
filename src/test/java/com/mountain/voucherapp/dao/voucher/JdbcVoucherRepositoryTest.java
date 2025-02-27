package com.mountain.voucherapp.dao.voucher;


import com.mountain.voucherapp.model.VoucherEntity;
import com.mountain.voucherapp.model.enums.DiscountPolicy;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
@ActiveProfiles("prod")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcVoucherRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcVoucherRepositoryTest.class);
    @Autowired
    JdbcVoucherRepository jdbcVoucherRepository;
    @Autowired
    DataSource dataSource;
    VoucherEntity voucherEntity;
    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp() {
        voucherEntity = new VoucherEntity(UUID.randomUUID(), DiscountPolicy.FIXED, 1000L);
        MysqldConfig config = aMysqldConfig(v8_0_11)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order-mgmt", classPathScript("schema.sql"))
                .start();
    }

    @AfterAll
    void cleanup() {
        embeddedMysql.stop();
    }

    @BeforeEach
    void init() {
        jdbcVoucherRepository.insert(voucherEntity);
    }

    @AfterEach
    void clear() {
        jdbcVoucherRepository.deleteById(voucherEntity.getVoucherId());
    }

    @Test
    @Description("dataSource가 정상적으로 Autowired되었는지 확인.")
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @DisplayName("바우처 추가할 수 있다.")
    public void testInsert() throws Exception {
        VoucherEntity percentVoucher = new VoucherEntity(UUID.randomUUID(), DiscountPolicy.PERCENT, 10L);
        try {
            jdbcVoucherRepository.insert(percentVoucher);
        } catch (BadSqlGrammarException e) {
            log.info("Got BadSqlGrammarException error code -> {}", e.getSQLException().getErrorCode(), e);
        }
        Optional<VoucherEntity> savedVoucher = jdbcVoucherRepository.findById(percentVoucher.getVoucherId());
        assertThat(savedVoucher.isEmpty(), is(false));
        assertThat(savedVoucher.get(), samePropertyValuesAs(percentVoucher));
        jdbcVoucherRepository.deleteById(percentVoucher.getVoucherId());
    }

    @Test
    @DisplayName("전체 바우처 조회.")
    public void testFindAll() throws Exception {
        List<VoucherEntity> vouchers = jdbcVoucherRepository.findAll();
        assertThat(vouchers.isEmpty(), is(false));
    }

    @Test
    @DisplayName("바우처를 수정할 수 있다.")
    public void testUpdate() throws Exception {
        voucherEntity.changeVoucherInfo(DiscountPolicy.PERCENT, 11L);
        jdbcVoucherRepository.update(voucherEntity);

        List<VoucherEntity> vouchers = jdbcVoucherRepository.findAll();
        assertThat(vouchers, hasSize(1));
        assertThat(vouchers, everyItem(samePropertyValuesAs(voucherEntity)));

        Optional<VoucherEntity> savedVoucher = jdbcVoucherRepository.findById(voucherEntity.getVoucherId());
        assertThat(savedVoucher.isEmpty(), is(false));
        assertThat(savedVoucher.get(), samePropertyValuesAs(voucherEntity));
    }

    @Test
    @DisplayName("할인 정책과 금액을 조건으로 데이터를 검색할 수 있다.")
    public void testFindDiscountPolicyAndAmount() throws Exception {
        Optional<VoucherEntity> existVoucher = jdbcVoucherRepository.findByDiscountPolicyAndAmount(voucherEntity.getDiscountPolicy(), voucherEntity.getDiscountAmount());
        Optional<VoucherEntity> notExistVoucher = jdbcVoucherRepository.findByDiscountPolicyAndAmount(voucherEntity.getDiscountPolicy(), voucherEntity.getDiscountAmount() * 2);

        assertThat(existVoucher.isEmpty(), is(false));
        assertThat(notExistVoucher.isEmpty(), is(true));
        assertThat(existVoucher.get(), samePropertyValuesAs(voucherEntity));
    }

    @Test
    @DisplayName("voucherId로 데이터를 삭제할 수 있다.")
    public void deleteByVoucherIdTest() {
        jdbcVoucherRepository.deleteById(voucherEntity.getVoucherId());

        Optional<VoucherEntity> voucher = jdbcVoucherRepository.findById(voucherEntity.getVoucherId());
        assertThat(voucher.isPresent(), is(false));
    }

    @Configuration
    @ComponentScan(
            basePackages = {"com.mountain.voucherapp.dao.voucher"}
    )
    static class Config {
        @Bean
        public DataSource dataSource() {
            String URL = "jdbc:mysql://localhost:2215/test-order-mgmt";
            HikariDataSource dataSource = DataSourceBuilder.create()
                    .url(URL)
                    .username("test")
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
            return dataSource;
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
            return new TransactionTemplate(platformTransactionManager);
        }
    }

}