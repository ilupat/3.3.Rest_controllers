getAllUsers()

function getAllUsers() {
    $('#table').empty()
    $.getJSON('/admin/list', function (data) {
        $.each(data, function (key, user) {
            let roleAss = []
            for (let i = 0; i < user.roles.length; i++) {
                roleAss.push(user.roles[i].name.replace('ROLE_', ''))
            }
            $('#tableAllUsers').append($('<tr>').append(
                    $('<td>').text(user.id),
                    $('<td>').text(user.username),
                    $('<td>').text(user.lastname),
                    $('<td>').text(user.age),
                    $('<td>').text(user.email),
                    $('<td>').text(roleAss.join(', ')),
                    $('<td>').append(
                        '<button type=\'button\' data-toggle=\'modal\' class=\'btn-info btn\'' +
                        ' data-target=\'#editUserModal\' data-user-id=\'' + user.id +
                        '\'>Edit</button>'),
                    $('<td>').append(
                        '<button type=\'button\' data-toggle=\'modal\' class=\'btn btn-danger\'' +
                        ' data-target=\'#deleteUserModal\' data-user-id=\'' + user.id +
                        '\'>Delete</button>'),
                ),
            )
        })
    })
    $('[href="#admin"]').on('show.bs.tab', (e) => {
        location.reload()
    })
}

$('#deleteUserModal').on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data('user-id')

    $.ajax({
        url: '/admin/' + userId,
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))

        $('#delId').empty().val(user.id)
        $('#delName').empty().val(user.username)
        $('#delName2').empty().val(user.lastname)
        $('#delAge').empty().val(user.age)
        $('#delEmail').empty().val(user.email)
        let roles = '';
        for (let i = 0; i < user.roles.length; i++) {
            roles += '<option value="' + user.roles[i].name.replace('ROLE_', '') + '">' + user.roles[i].name.replace('ROLE_', '') + '</option>';
        }
        $('#delRole').empty().html(roles);

        $('#buttonDel').on('click', (e) => {
            e.preventDefault()

            $.ajax({
                url: '/admin/' + userId,
                type: 'DELETE',
                dataType: 'text',
            }).done((deleteMsg) => {
                $(`#deleteUserModal`).modal('hide')
                location.reload()
            })
        })
    })
})
$('[href="#userss"]').on('show.bs.tab', (e) => {
    $('#change-tabContent').hide(),
        getCurrent()
})

function getCurrent() {
    $.ajax({
        url: '/getUser',
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))
        let roleAss = []
        for (let i = 0; i < user.roles.length; i++) {
            roleAss.push(user.roles[i].name.replace('ROLE_', ''))
        }
        $('#current-user-table tbody').empty().append(
            $('<tr>').append(
                $('<td>').text(user.id),
                $('<td>').text(user.username),
                $('<td>').text(user.lastname),
                $('<td>').text(user.age),
                $('<td>').text(user.email),
                $('<td>').text(roleAss.join(', ')),
            ))
    }).fail(() => {
        alert('Error Get All Users!')
    })
}

$('#editUserModal').on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data('user-id')

    $.ajax({
        url: '/admin/' + userId,
        type: 'GET',
        dataType: 'json',
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg))
        $.getJSON('admin/roles', function (editRole) {
            $('#id').empty().val(user.id)
            $('#editName').empty().val(user.username)
            $('#editName2').empty().val(user.lastname)
            $('#editAge').empty().val(user.age)
            $('#editEmail').empty().val(user.email)
            $('#editPassword').val('')
            $('#editRoles').empty()
            $.each(editRole, (i, role) => {
                $('#editRoles').append($('<option>').text(role.name.replace('ROLE_', '')).attr('value', role.name))
            })
            $('#editRoles').val(user.roles)
        })
    })
})

$('#buttonEditSubmit').on('click', (e) => {
    e.preventDefault()

    let editUser = {
        id: $('#id').val(),
        username: $('#editName').val(),
        lastname: $('#editName2').val(),
        age: $('#editAge').val(),
        email: $('#editEmail').val(),
        password: $('#editPassword').val(),
        roleNames: $('#editRoles').val(),
    }

    $.ajax({
        url: '/admin',
        type: 'PUT',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(editUser),
    })

    $('#editUserModal').modal('hide')
    location.reload()
    getAllUsers()
})

$('[href="#new"]').on('show.bs.tab', (e) => {
    $.getJSON('admin/roles', function (newUser) {
        $(() => {
            $('#name').empty().val('')
            $('#name2').empty().val('')
            $('#age').empty().val('')
            $('#email').empty().val('')
            $('#password').empty().val('')
            $('#rolesNew').empty().val('')
            $.each(newUser, function (k, role) {
                $('#rolesNew').append(
                    $('<option>').text(role.name.replace('ROLE_', '')).attr('value', role.name),
                )
            })
        })
    })
})
$('#buttonNew').on('click', (e) => {
    e.preventDefault()
    let newUser = {
        username: $('#name').val(),
        lastname: $('#name2').val(),
        age: $('#age').val(),
        email: $('#email').val(),
        password: $('#password').val(),
        roleNames: $('#rolesNew').val(),
    }
    let json = JSON.stringify(newUser);
    console.log(json);

    $.ajax({
        url: '/admin',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: json,
    })

    getAllUsers(),
        $('#testTab a[href="#usersTable"]').tab('show')
    location.reload()
})
