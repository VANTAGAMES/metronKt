import korlibs.image.atlas.Atlas
import korlibs.io.file.VfsFile
import korlibs.io.file.std.resourcesVfs
import korlibs.image.atlas.readAtlas
import korlibs.audio.sound.readSound
import korlibs.image.format.readBitmap

// AUTO-GENERATED FILE! DO NOT MODIFY!

@Retention(AnnotationRetention.BINARY) annotation class ResourceVfsPath(val path: String)
inline class TypedVfsFile(val __file: VfsFile)
inline class TypedVfsFileBitmap(val __file: VfsFile) { suspend fun read(): korlibs.image.bitmap.Bitmap = this.__file.readBitmap() }
inline class TypedVfsFileSound(val __file: VfsFile) { suspend fun read(): korlibs.audio.sound.Sound = this.__file.readSound() }
interface TypedAtlas<T>

object KR : __KR.KR

object __KR {
	
	interface KR {
		val __file get() = resourcesVfs[""]
		@ResourceVfsPath("client.properties") val `client` get() = TypedVfsFile(resourcesVfs["client.properties"])
		@ResourceVfsPath("fonts") val `fonts` get() = __KR.KRFonts
	}
	
	object KRFonts {
		val __file get() = resourcesVfs["fonts"]
		@ResourceVfsPath("fonts/NanumSquareNeoTTF-dEb.woff") val `nanumsquareneottfDeb` get() = TypedVfsFile(resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"])
		@ResourceVfsPath("fonts/NanumSquareNeoTTF-eHv.woff") val `nanumsquareneottfEhv` get() = TypedVfsFile(resourcesVfs["fonts/NanumSquareNeoTTF-eHv.woff"])
	}
}
