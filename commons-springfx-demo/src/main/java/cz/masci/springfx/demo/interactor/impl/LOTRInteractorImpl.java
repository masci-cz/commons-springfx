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

/**
 * Spring service implementation of {@link LOTRInteractor} using {@link Faker} to generate demo data.
 */
@Service
@RequiredArgsConstructor
public class LOTRInteractorImpl implements LOTRInteractor {

  /** Faker instance used to generate fake LOTR character data. */
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

  /**
   * Creates a new {@link LOTRDetailModel} for the given numeric id using fake data.
   *
   * @param id the numeric identifier to assign to the character
   * @return a new {@link LOTRDetailModel} populated with fake data
   */
  private LOTRDetailModel nextCharacter(long id) {
    return map(faker.lordOfTheRings(), id);
  }

  /**
   * Maps a {@link LordOfTheRings} faker instance to a {@link LOTRDetailModel}.
   *
   * @param origin the faker providing character and location data
   * @param id     the identifier to assign to the model
   * @return a baselined {@link LOTRDetailModel}
   */
  private LOTRDetailModel map(LordOfTheRings origin, long id) {
    var model = new LOTRDetailModel();
    model.setId(id);
    model.setCharacter(origin.character());
    model.setLocation(origin.location());
    model.rebaseline();
    return model;
  }
}
