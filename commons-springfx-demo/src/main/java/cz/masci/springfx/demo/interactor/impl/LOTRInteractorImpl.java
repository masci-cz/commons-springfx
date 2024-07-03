/*
 * Copyright (c) 2024
 *
 * This file is part of commons-springfx library.
 *
 * commons-springfx library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * commons-springfx library is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.demo.interactor.impl;

import com.github.javafaker.Faker;
import com.github.javafaker.LordOfTheRings;
import cz.masci.springfx.demo.interactor.LOTRInteractor;
import cz.masci.springfx.demo.model.LOTRDetailModel;
import java.util.List;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LOTRInteractorImpl implements LOTRInteractor {

  private final Faker faker;

  @Override
  public List<LOTRDetailModel> loadCharacters() {
    return LongStream.range(0, 10)
        .mapToObj(this::nextCharacter)
        .peek(unused -> {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        })
        .toList();
  }

  @Override
  public LOTRDetailModel saveCharacter(LOTRDetailModel model) {
    model.setId(model.isTransient() ? faker.random().nextLong() : model.getId());

    return model;
  }

  private LOTRDetailModel nextCharacter(long id) {
    return map(faker.lordOfTheRings(), id);
  }

  private LOTRDetailModel map(LordOfTheRings origin, long id) {
    var model = new LOTRDetailModel();
    model.setId(id);
    model.setCharacter(origin.character());
    model.setLocation(origin.location());
    model.rebaseline();
    return model;
  }
}
