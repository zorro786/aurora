package com.twitter.mesos.executor;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.twitter.mesos.codec.ThriftBinaryCodec;
import com.twitter.mesos.gen.AssignedTask;
import com.twitter.mesos.gen.ScheduleStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class to hold constants related to launched tasks.
 *
 * @author wfarner
 */
public class TaskUtils {

  // Path to file (relative to sandbox) for the serialized TwitterTaskInfo dump file.
  static final String TASK_DUMP_FILE = "task.dump";

  // Path to file (relative to sandbox) for the serialized task status.
  static final String TASK_STATUS_FILE = "task.status";

  private static final FileToInt STATUS_FETCHER = new FileToInt();

  static void storeTask(File taskRoot, AssignedTask task) throws IOException,
      ThriftBinaryCodec.CodingException {
    checkTaskRoot(taskRoot);
    Preconditions.checkNotNull(task);

    Files.write(ThriftBinaryCodec.encode(task), new File(taskRoot, TASK_DUMP_FILE));
  }

  static AssignedTask fetchTask(File taskRoot) throws IOException,
      ThriftBinaryCodec.CodingException {
    checkTaskRoot(taskRoot);

    File serializedTask = new File(taskRoot, TASK_DUMP_FILE);
    if (!serializedTask.exists()) {
      throw new FileNotFoundException("Expected to find task dump file: " + serializedTask);
    }

    return ThriftBinaryCodec.decode(AssignedTask.class, Files.toByteArray(serializedTask));
  }

  static void saveTaskStatus(File taskRoot, ScheduleStatus status) throws IOException {
    Files.write(String.valueOf(status.getValue()), new File(taskRoot, TASK_STATUS_FILE),
        Charsets.US_ASCII);
  }

  static ScheduleStatus getTaskStatus(File taskRoot)
      throws FileToInt.FetchException, FileNotFoundException {
    File statusFile = new File(taskRoot, TASK_STATUS_FILE);
    if (!statusFile.exists()) {
      throw new FileNotFoundException("Status file does not exist: " + statusFile);
    }

    return ScheduleStatus.findByValue(STATUS_FETCHER.apply(statusFile));
  }

  private static void checkTaskRoot(File taskRoot) {
    Preconditions.checkNotNull(taskRoot);
    Preconditions.checkArgument(taskRoot.exists(), "Task root does not exist: " + taskRoot);
    Preconditions.checkArgument(taskRoot.isDirectory(), "Not a directory: " + taskRoot);
  }

  private TaskUtils() {
    // Utility.
  }
}
