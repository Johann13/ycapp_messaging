#import "YcappmessagingPlugin.h"
#if __has_include(<ycappmessaging/ycappmessaging-Swift.h>)
#import <ycappmessaging/ycappmessaging-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "ycappmessaging-Swift.h"
#endif

@implementation YcappmessagingPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftYcappmessagingPlugin registerWithRegistrar:registrar];
}
@end
