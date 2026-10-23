"""cap add android চালানোর পরে native কোড ও Manifest বসিয়ে দেয়।"""
import re, shutil, pathlib

root = pathlib.Path(__file__).resolve().parent.parent
java_dir = root / "android/app/src/main/java/com/fajr/azan"
java_dir.mkdir(parents=True, exist_ok=True)
for f in (root / "native").glob("*.java"):
    shutil.copy(f, java_dir / f.name)

mf = root / "android/app/src/main/AndroidManifest.xml"
x = mf.read_text(encoding="utf-8")

perms = """
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
    <uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
"""
comps = """
        <receiver android:name=".AlarmReceiver" android:exported="false" />
        <receiver android:name=".BootReceiver" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
        <service android:name=".AlarmService" android:exported="false"
                 android:foregroundServiceType="mediaPlayback" />
"""
if "AlarmService" not in x:
    x = x.replace("</manifest>", perms + "</manifest>")
    x = x.replace("</application>", comps + "    </application>")
    # লক স্ক্রিনের ওপরে খুলবে ও স্ক্রিন জ্বালাবে
    x = re.sub(r"(<activity\b)", r'\1 android:showWhenLocked="true" android:turnScreenOn="true"', x, count=1)
    mf.write_text(x, encoding="utf-8")
print("patched OK")
