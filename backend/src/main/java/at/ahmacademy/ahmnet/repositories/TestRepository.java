package at.ahmacademy.ahmnet.repositories;

import at.ahmacademy.ahmnet.model.TestClass;

public interface TestRepository extends AbstractRepository<TestClass, Long> {

  TestClass findFirstById(Long id);

}

