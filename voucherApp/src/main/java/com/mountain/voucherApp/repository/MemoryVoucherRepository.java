package com.mountain.voucherApp.repository;

import com.mountain.voucherApp.voucher.Voucher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Primary
// @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Repository
// @Qualifier("memory")
@Profile({"local", "default"})
public class MemoryVoucherRepository implements VoucherRepository {

   private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();

   @Override
   public Optional<Voucher> findById(UUID voucherId) {
       return Optional.ofNullable(storage.get(voucherId));
   }

   @Override
   public List<Voucher> findAll() {
      return storage.values()
              .stream()
              .collect(Collectors.toList());
   }

   @Override
    public Voucher insert(Voucher voucher) {
       storage.put(voucher.getVoucherId(), voucher);
       return voucher;
   }
}
