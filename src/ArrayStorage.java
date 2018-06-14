import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int elementsCount = 0;

    void clear() {
        Arrays.fill(storage, 0, elementsCount, null);
        elementsCount = 0;
    }

    void save(Resume r) {
        storage[elementsCount++] = r;
    }

    Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) return storage[index];
        return null;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < elementsCount; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index + 1 - 1, elementsCount - (index + 1));
            elementsCount--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, elementsCount);
    }

    int size() {
        return elementsCount;
    }
}
