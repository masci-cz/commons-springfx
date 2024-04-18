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
import cz.masci.springfx.demo.interactor.BookInteractor;
import cz.masci.springfx.demo.model.BookDetailModel;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInteractorImpl implements BookInteractor {

  private final Faker faker;

  @Override
  public List<BookDetailModel> list() {
    var book = faker.book();
    var bookDetailModel = new BookDetailModel();
    bookDetailModel.setAuthor(book.author());
    bookDetailModel.setTitle(book.title());
    bookDetailModel.rebaseline();
    return Arrays.asList(bookDetailModel);
  }

  @Override
  public BookDetailModel save(BookDetailModel book) {
    if (book.getId() == null) {
      book.setId(faker.random().nextLong());
    }
    book.rebaseline();
    return book;
  }

  @Override
  public void delete(BookDetailModel book) {
    book.setId(null);
  }
}
