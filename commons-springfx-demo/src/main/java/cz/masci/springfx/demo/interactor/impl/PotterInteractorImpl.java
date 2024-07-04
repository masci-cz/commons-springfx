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
import com.github.javafaker.HarryPotter;
import cz.masci.springfx.demo.interactor.PotterInteractor;
import cz.masci.springfx.demo.model.PotterDetailModel;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PotterInteractorImpl implements PotterInteractor {

  private final Faker faker;

  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public PotterDetailModel addCharacter() {
    return map(faker.harryPotter());
  }

  private PotterDetailModel map(HarryPotter harryPotterItem) {
    var model = new PotterDetailModel();
    model.setId(idGenerator.getAndIncrement());
    model.setBook(harryPotterItem.book());
    model.setCharacter(harryPotterItem.character());
    model.setLocation(harryPotterItem.location());
    model.setQuote(harryPotterItem.quote());
    model.rebaseline();
    return model;
  }
}
