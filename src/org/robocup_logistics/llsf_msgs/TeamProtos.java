// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Team.proto

package org.robocup_logistics.llsf_msgs;

public final class TeamProtos {
  private TeamProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code llsf_msgs.Team}
   *
   * <pre>
   * Team identifier.
   * </pre>
   */
  public enum Team
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>CYAN = 0;</code>
     */
    CYAN(0, 0),
    /**
     * <code>MAGENTA = 1;</code>
     */
    MAGENTA(1, 1),
    ;

    /**
     * <code>CYAN = 0;</code>
     */
    public static final int CYAN_VALUE = 0;
    /**
     * <code>MAGENTA = 1;</code>
     */
    public static final int MAGENTA_VALUE = 1;


    public final int getNumber() { return value; }

    public static Team valueOf(int value) {
      switch (value) {
        case 0: return CYAN;
        case 1: return MAGENTA;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Team>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<Team>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Team>() {
            public Team findValueByNumber(int number) {
              return Team.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return org.robocup_logistics.llsf_msgs.TeamProtos.getDescriptor().getEnumTypes().get(0);
    }

    private static final Team[] VALUES = values();

    public static Team valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private Team(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:llsf_msgs.Team)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nTeam.proto\022\tllsf_msgs*\035\n\004Team\022\010\n\004CYAN\020" +
      "\000\022\013\n\007MAGENTA\020\001B-\n\037org.robocup_logistics." +
      "llsf_msgsB\nTeamProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
