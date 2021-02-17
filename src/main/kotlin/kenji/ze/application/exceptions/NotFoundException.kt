package kenji.ze.application.exceptions

class NotFoundException(resource: String) : RuntimeException("Resource $resource not found")