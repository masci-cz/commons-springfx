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

package cz.masci.springfx.mvci.util.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.concurrent.Task;
import org.apache.commons.lang3.function.FailableFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class BackgroundTaskBuilderTest {

  @Mock
  private Callable<String> callableMock;
  @Mock
  private FailableFunction<Task<String>, String, Exception> functionalMock;
  @Mock
  private Consumer<String> consumerMock;
  @Mock
  private Consumer<Task<String>> consumerTaskMock;
  @Mock
  private Runnable runnableMock;

  // region tests
  @Test
  void callableTask() {
    var taskBuilder = BackgroundTaskBuilder.task(callableMock);

    assertNotNull(taskBuilder);
    verifyNoInteractions(callableMock);
  }

  @Test
  void callableTask_start() throws Exception {
    String RESULT = "Hello";
    when(callableMock.call()).thenReturn(RESULT);

    BackgroundTaskBuilder.task(callableMock).start();
    sleep();

    verify(callableMock, atLeastOnce()).call();
  }

  @Test
  void functionalTask() {
    var taskBuilder = BackgroundTaskBuilder.task(functionalMock);

    assertNotNull(taskBuilder);
    verifyNoInteractions(functionalMock);
  }

  @Test
  void functionalTask_start() throws Exception {
    BackgroundTaskBuilder.task(functionalMock).start();
    sleep();

    verify(functionalMock, atLeastOnce()).apply(any());
  }

  @Test
  void onSucceeded() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .onSucceeded(consumerMock)
        .start();
    sleep();

    verify(consumerMock, only()).accept(any());
  }

  @Test
  void onSucceeded_notCalled() throws Exception {
    BackgroundTaskBuilder.task(this::cancelTask)
        .onSucceeded(consumerMock)
        .start();
    sleep();

    verifyNoInteractions(consumerMock);
  }

  @Test
  void onCancelled() throws Exception {
    BackgroundTaskBuilder.task(this::cancelTask)
        .onCancelled(consumerTaskMock)
        .start();
    sleep();

    verify(consumerTaskMock, only()).accept(any());
  }

  @Test
  void onCancelled_notCalled() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .onCancelled(consumerTaskMock)
        .start();
    sleep();

    verifyNoInteractions(consumerTaskMock);
  }

  @Test
  void onFailed() throws Exception {
    BackgroundTaskBuilder.task(this::failTask)
        .onFailed(consumerTaskMock)
        .start();
    sleep();

    verify(consumerTaskMock, only()).accept(any());
  }

  @Test
  void onFailed_notCalled() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .onFailed(consumerTaskMock)
        .start();
    sleep();

    verifyNoInteractions(consumerTaskMock);
  }

  @Test
  void onRunning() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .onRunning(consumerTaskMock)
        .start();
    sleep();

    verify(consumerTaskMock, only()).accept(any());
  }

  @Test
  void onScheduled() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .onScheduled(consumerTaskMock)
        .start();
    sleep();

    verify(consumerTaskMock, only()).accept(any());
  }

  @Test
  void postGuiCall_onSucceeded() throws Exception {
    BackgroundTaskBuilder.task(this::normalTask)
        .postGuiCall(runnableMock)
        .start();
    sleep();

    verify(runnableMock, only()).run();
  }

  @Test
  void postGuiCall_onCancelled() throws Exception {
    BackgroundTaskBuilder.task(this::cancelTask)
        .postGuiCall(runnableMock)
        .start();
    sleep();

    verify(runnableMock, only()).run();
  }

  @Test
  void postGuiCall_onFailed() throws Exception {
    BackgroundTaskBuilder.task(this::failTask)
        .postGuiCall(runnableMock)
        .start();
    sleep();

    verify(runnableMock, only()).run();
  }
  // endregion

  // region utils
  private String cancelTask(Task<String> task) {
    task.cancel();
    return "";
  }

  private String failTask(Task<String> task) throws Exception {
    throw new Exception();
  }

  private String normalTask() {
    return "Hello";
  }

  private void sleep() throws InterruptedException {
    Thread.sleep(200);
  }
  // endregion
}