function confirmDelete(userId) {
    if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
    window.location.href = "/admin/supp?id=" + userId;
}
}
