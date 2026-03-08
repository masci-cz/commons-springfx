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

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javafx.concurrent.Task;
import org.apache.commons.lang3.function.FailableFunction;

/**
 * This builder prepares {@link Task} with most events handlers.
 *
 * <p>
 *   <b>Basic usage</b>
 * </p>
 *
 * <pre>{@code
 *   BackgroundTaskBuilder.task(() -> "Hello").start();
 * }</pre>
 *
 * <p>
 *   <b>All event handlers</b>
 * </p>
 *
 * <pre>{@code
 *   BackgroundTaskBuilder
 *      .task(() -> "Hello")
 *      .onCancelled(task -> System.out.println("Task has been cancelled"))
 *      .onFailed(task -> System.out.println("Task failed with an error: " + task.getException()))
 *      .onRunning(task -> System.out.println("Task has been started"))
 *      .onScheduled(task -> System.out.println("Task has been scheduled"))
 *      .onSucceeded(taskResult -> System.out.println("Task ended with result: " + taskResult))
 *      .postGuiCall(() -> System.out.println("This part is running in JavaFX thread"))
 *      .start();
 * }</pre>
 *
 * <p>
 *   <b>Post GUI Call</b>
 * </p>
 *
 * {@code postGuiCall} is called when {@link Task} succeeded, cancelled or failed after appropriate onXXX method is called.
 *
 * @param <T>  the type of the background task result
 */
public class BackgroundTaskBuilder<T> {
  /** The callable task to execute in the background. */
  private Callable<T> callableTask;
  /** The functional task (with access to the Task itself) to execute in the background. */
  private FailableFunction<Task<T>, T, ? extends Exception> functionalTask;
  /** Runnable executed on the JavaFX thread after the task finishes (success, cancel, or failure). */
  private Runnable postGuiCall;
  /** Handler invoked when the task is cancelled. */
  private Consumer<Task<T>> onCancelled;
  /** Handler invoked when the task fails. */
  private Consumer<Task<T>> onFailed;
  /** Handler invoked when the task starts running. */
  private Consumer<Task<T>> onRunning;
  /** Handler invoked when the task is scheduled. */
  private Consumer<Task<T>> onScheduled;
  /** Handler invoked with the result value when the task succeeds. */
  private Consumer<T> onSucceeded;

  /**
   * Creates a new {@code BackgroundTaskBuilder} with the given callable task.
   *
   * @param callableTask the callable to execute in the background
   */
  private BackgroundTaskBuilder(Callable<T> callableTask) {
    this.callableTask = callableTask;
  }

  /**
   * Creates a new {@code BackgroundTaskBuilder} with the given functional task.
   *
   * @param functionalTask the function receiving the {@link Task} and returning the result
   */
  private BackgroundTaskBuilder(FailableFunction<Task<T>, T, ? extends Exception> functionalTask) {
    this.functionalTask = functionalTask;
  }

  /**
   * Creates a new builder for the given callable task.
   *
   * @param callableTask the callable to execute in the background
   * @param <T>          the result type of the task
   * @return a new {@code BackgroundTaskBuilder}
   */
  public static <T> BackgroundTaskBuilder<T> task(Callable<T> callableTask) {
    return new BackgroundTaskBuilder<>(callableTask);
  }

  /**
   * Creates a new builder for the given functional task.
   *
   * @param functionalTask the function receiving the {@link Task} and returning the result
   * @param <T>            the result type of the task
   * @return a new {@code BackgroundTaskBuilder}
   */
  public static <T> BackgroundTaskBuilder<T> task(FailableFunction<Task<T>, T, ? extends Exception> functionalTask) {
    return new BackgroundTaskBuilder<>(functionalTask);
  }

  /**
   * Builds the {@link Task} and starts it on a new daemon thread.
   */
  public void start() {
    Task<T> task = build();

    Thread thread = new Thread(task);
    thread.start();
  }

  /**
   * Builds and returns the configured {@link Task}.
   *
   * @return a fully configured {@link Task} ready to be executed
   */
  public Task<T> build() {
    Task<T> task = new Task<>() {
      @Override
      protected T call() throws Exception {
        if (callableTask != null) {
          return callableTask.call();
        }
        if (functionalTask != null) {
          return functionalTask.apply(this);
        }
        return null;
      }

    };

    setOnCancelled(task);
    setOnFailed(task);
    setOnRunning(task);
    setOnScheduled(task);
    setOnSucceeded(task);

    return task;
  }

  /**
   * Registers the onCancelled handler on the task, followed by the postGuiCall if set.
   *
   * @param task the task to configure
   */
  private void setOnCancelled(Task<T> task) {
    task.setOnCancelled(evt -> {
      if (onCancelled != null) {
        onCancelled.accept(task);
      }
      if (postGuiCall != null) {
        postGuiCall.run();
      }
    });
  }

  /**
   * Registers the onFailed handler on the task, followed by the postGuiCall if set.
   *
   * @param task the task to configure
   */
  private void setOnFailed(Task<T> task) {
    task.setOnFailed(evt -> {
      if (onFailed != null) {
        onFailed.accept(task);
      }
      if (postGuiCall != null) {
        postGuiCall.run();
      }
    });
  }

  /**
   * Registers the onRunning handler on the task.
   *
   * @param task the task to configure
   */
  private void setOnRunning(Task<T> task) {
    task.setOnRunning(evt -> {
      if (onRunning != null) {
        onRunning.accept(task);
      }
    });
  }

  /**
   * Registers the onScheduled handler on the task.
   *
   * @param task the task to configure
   */
  private void setOnScheduled(Task<T> task) {
    task.setOnScheduled(evt -> {
      if (onScheduled != null) {
        onScheduled.accept(task);
      }
    });
  }

  /**
   * Registers the onSucceeded handler on the task, followed by the postGuiCall if set.
   *
   * @param task the task to configure
   */
  private void setOnSucceeded(Task<T> task) {
    task.setOnSucceeded(evt -> {
      if (onSucceeded != null) {
        onSucceeded.accept(task.getValue());
      }
      if (postGuiCall != null) {
        postGuiCall.run();
      }
    });
  }

  /**
   * Sets the runnable to execute on the JavaFX thread after the task finishes.
   *
   * @param postGuiCall the runnable to execute after task completion
   * @return this builder
   */
  public BackgroundTaskBuilder<T> postGuiCall(Runnable postGuiCall) {
    this.postGuiCall = postGuiCall;

    return this;
  }

  /**
   * Sets the handler to invoke when the task is cancelled.
   *
   * @param onCancelled consumer receiving the cancelled task
   * @return this builder
   */
  public BackgroundTaskBuilder<T> onCancelled(Consumer<Task<T>> onCancelled) {
    this.onCancelled = onCancelled;

    return this;
  }

  /**
   * Sets the handler to invoke when the task fails.
   *
   * @param onFailed consumer receiving the failed task
   * @return this builder
   */
  public BackgroundTaskBuilder<T> onFailed(Consumer<Task<T>> onFailed) {
    this.onFailed = onFailed;

    return this;
  }

  /**
   * Sets the handler to invoke when the task starts running.
   *
   * @param onRunning consumer receiving the running task
   * @return this builder
   */
  public BackgroundTaskBuilder<T> onRunning(Consumer<Task<T>> onRunning) {
    this.onRunning = onRunning;

    return this;
  }

  /**
   * Sets the handler to invoke when the task is scheduled.
   *
   * @param onScheduled consumer receiving the scheduled task
   * @return this builder
   */
  public BackgroundTaskBuilder<T> onScheduled(Consumer<Task<T>> onScheduled) {
    this.onScheduled = onScheduled;

    return this;
  }

  /**
   * Sets the handler to invoke with the result value when the task succeeds.
   *
   * @param onSucceeded consumer receiving the task result
   * @return this builder
   */
  public BackgroundTaskBuilder<T> onSucceeded(Consumer<T> onSucceeded) {
    this.onSucceeded = onSucceeded;

    return this;
  }
}
